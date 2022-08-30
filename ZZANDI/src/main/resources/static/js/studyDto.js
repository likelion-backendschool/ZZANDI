

//달력
$(".a1").change(function () {
    const val = $(this).val();
    console.log(val);
    $(".a2").attr("min", val);
});


//태그
var input = document.querySelector('input[name=studyTag]');

new Tagify(input, {
    whitelist: ["IT","소설책","영어"], // 화이트리스트 드롭다운 메뉴 설정
    userInput: false // 입력 제한
})

//라디오버튼

$(document).ready(function(){

    $('#book').hide();
    $('#lecture').hide(); // 초깃값 설정

    $("input[name='studyType']").change(function(){
        if($("input[name='studyType']:checked").val() == 'BOOK'){
            $('#book').show();
            $('#lecture').hide();
        }
        else if($("input[name='studyType']:checked").val() == 'LECTURE'){
            $('#book').hide();
            $('#lecture').show();
        }
    });
});