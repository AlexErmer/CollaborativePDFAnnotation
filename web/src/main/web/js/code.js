$(function() {
    $('.selectAll').change(function(){
        var checkboxes = $(this).closest('form').find(':checkbox');
        if($(this).prop('checked')) {
          checkboxes.prop('checked', true);
        } else {
          checkboxes.prop('checked', false);
        }
    });
});

// On submit disable its submit button
$('form').submit(function(){
    $('input[type=submit]', this).attr('disabled', 'disabled');
});