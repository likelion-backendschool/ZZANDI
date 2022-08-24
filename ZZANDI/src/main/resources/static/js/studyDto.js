

//달력
$(".a1").change(function () {
    const val = $(this).val();
    console.log(val);
    $(".a2").attr("min", val);
});


//태그
var input = document.querySelector('input[name=studyTag]')
var tagify = new Tagify(input);

tagify.on('add', function() {

})

//라디오버튼

$(document).ready(function(){

    $('#book').hide();
    $('#lecture').hide(); // 초깃값 설정

    $("input[name='studyType']").change(function(){
        if($("input[name='studyType']:checked").val() == 'Book'){
            $('#book').show();
            $('#lecture').hide();
        }
        else if($("input[name='studyType']:checked").val() == 'Lecture'){
            $('#book').hide();
            $('#lecture').show();
        }
    });
});