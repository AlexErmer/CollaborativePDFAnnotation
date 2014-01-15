/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.cmdline.ArgumentParser;
import opennlp.tools.cmdline.ArgumentParser.OptionalParameter;
import opennlp.tools.cmdline.ArgumentParser.ParameterDescription;
import opennlp.tools.cmdline.TerminateToolException;
import opennlp.tools.formats.AbstractSampleStreamFactory;
import opennlp.tools.formats.brat.AnnotationConfiguration;
import opennlp.tools.formats.brat.BratDocument;
import opennlp.tools.formats.brat.BratDocumentStream;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.sentdetect.NewlineSentenceDetector;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.ObjectStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * direct copy of {@code opennlp.tools.formats.brat.BratNameSampleStreamFactory}! All credit belongs to them!!
 */
public class MyBratNameSampleStreamFactory extends AbstractSampleStreamFactory<NameSample> {

    interface Parameters {
        @ParameterDescription(valueName = "bratDataDir", description = "location of brat data dir")
        File getBratDataDir();

        @ParameterDescription(valueName = "annConfFile")
        File getAnnotationConfig();

        @ParameterDescription(valueName = "modelFile")
        @OptionalParameter
        File getSentenceDetectorModel();

        @ParameterDescription(valueName = "modelFile")
        @OptionalParameter
        File getTokenizerModel();

        @ParameterDescription(valueName = "name")
        @OptionalParameter
        String getRuleBasedTokenizer();

        @ParameterDescription(valueName = "value")
        @OptionalParameter(defaultValue = "false")
        Boolean getRecursive();

    }

    public MyBratNameSampleStreamFactory() {
        super(Parameters.class);
    }

    /**
     * Checks that non of the passed values are null.
     *
     * @param objects
     * @return
     */
    private boolean notNull(Object... objects) {

        for (Object obj : objects) {
            if (obj == null)
                return false;
        }

        return true;
    }

    public ObjectStream<NameSample> create(String[] args) {

        Parameters params = ArgumentParser.parse(args, Parameters.class);

        if (notNull(params.getRuleBasedTokenizer(), params.getTokenizerModel())) {
            throw new TerminateToolException(-1, "Either use rule based or statistical tokenizer!");
        }

        // TODO: Provide the file name to the annotation.conf file and implement the parser ...
        AnnotationConfiguration annConfig;
        InputStream annConfIn = null;
        try {
            annConfIn = new FileInputStream(params.getAnnotationConfig());
            annConfig = AnnotationConfiguration.parse(annConfIn);
        } catch (IOException e) {
            throw new TerminateToolException(1, "Failed to parse annotation.conf file!");
        } finally {
            if (annConfIn != null) {
                try {
                    annConfIn.close();
                } catch (IOException e) {
                }
            }
        }

        // TODO: Add an optional parameter to search recursive
        // TODO: How to handle the error here ? terminate the tool? not nice if used by API!
        ObjectStream<BratDocument> samples;
        try {
            samples = new BratDocumentStream(annConfig,
                    params.getBratDataDir(), params.getRecursive(), null);
        } catch (IOException e) {
            throw new TerminateToolException(-1, e.getMessage());
        }

        SentenceDetector sentDetector;

        if (params.getSentenceDetectorModel() != null) {
            try {
                sentDetector = new SentenceDetectorME(new SentenceModel(params.getSentenceDetectorModel()));
            } catch (IOException e) {
                throw new TerminateToolException(-1, "Failed to load sentence detector model!", e);
            }
        } else {
            sentDetector = new NewlineSentenceDetector();
        }

        Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;

        if (params.getTokenizerModel() != null) {
            try {
                tokenizer = new TokenizerME(new TokenizerModel(params.getTokenizerModel()));
            } catch (IOException e) {
                throw new TerminateToolException(-1, "Failed to load tokenizer model!", e);
            }
        } else if (params.getRuleBasedTokenizer() != null) {
            String tokenizerName = params.getRuleBasedTokenizer();

            if ("simple".equals(tokenizerName)) {
                tokenizer = SimpleTokenizer.INSTANCE;
            } else if ("whitespace".equals(tokenizerName)) {
                tokenizer = WhitespaceTokenizer.INSTANCE;
            } else {
                throw new TerminateToolException(-1, "Unkown tokenizer: " + tokenizerName);
            }
        }

        return new MyBratNameSampleStream(sentDetector, tokenizer, samples);
    }
}
