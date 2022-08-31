

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

//책 검색

$(document).ready(function (){
    $('#button1').hide();
    $('#button2').hide();
    $("#search").on('click', function () {
        $('#button1').show();
        $('#button2').show();
        $.ajax({
            method: "GET",
            url: "http://localhost:8080/study/search/book",
            data: {
                query: $("#bookSearch").val(),
            },
        })
            .done(function (msg) {
                console.log(msg);
                $(".card-title").empty();
                $(".card-author").empty();
                $(".card-publisher").empty();
                $(".thumbnail").empty();
                $(".card-title").append("제목: " + msg.documents[0].title);
                $(".card-author").append("저자: " + msg.documents[0].authors);
                $(".card-publisher").append("출판사: " + msg.documents[0].publisher);
                $(".thumbnail").append('<img src="' + msg.documents[0].thumbnail + '"/>');
                $(".card-title2").empty();
                $(".card-author2").empty();
                $(".card-publisher2").empty();
                $(".thumbnail2").empty();
                $(".card-title2").append("제목: " + msg.documents[1].title);
                $(".card-author2").append("저자: " + msg.documents[1].authors);
                $(".card-publisher2").append("출판사: " + msg.documents[1].publisher);
                $(".thumbnail2").append('<img src="' + msg.documents[1].thumbnail + '"/>');
                $("#button1").on('click', function () {
                    $('input[name=bookName]').attr('value', msg.documents[0].title);
                    $('input[name=bookAuthor]').attr('value', msg.documents[0].authors);
                    $('input[name=bookPublisher]').attr('value', msg.documents[0].publisher);
                    $('input[name=bookIsbn]').attr('value', msg.documents[0].isbn);
                });
                $("#button2").on('click', function () {
                    $('input[name=bookName]').attr('value', msg.documents[0].title);
                    $('input[name=bookAuthor]').attr('value', msg.documents[1].authors);
                    $('input[name=bookPublisher]').attr('value', msg.documents[1].publisher);
                    $('input[name=bookIsbn]').attr('value', msg.documents[1].isbn);
                });
            });
    });

});
// 엔터키 방지
document.myForm.addEventListener("keydown", evt => {
    if (evt.code === "Enter") evt.preventDefault();
});