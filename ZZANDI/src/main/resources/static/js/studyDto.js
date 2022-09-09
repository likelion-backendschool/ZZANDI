

//달력
$("#studyStart").change(function () {
    const val = $(this).val();
    $("#studyEnd").attr("min", val);
});

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

$("#submit").on('click', function () {
    var searchName = document.getElementById("bookSearch").value;
    var bookName = document.getElementById("bookName").value;
    if($("input[name='studyType']:checked").val() == 'BOOK') {
        if (searchName == '') {
            alert("책 제목을 입력해주십시오.");
            $('#bookSearch').focus();
            return false;
        }
        if (bookName == '') {
            alert("책을 선택해주세요.");
            $('#bookSearch').focus();
            return false;
        }
    }
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
                console.log(msg.item[0].title);
                console.log(msg.item[0].isbn13);

                $(".card-title").empty();
                $(".card-author").empty();
                $(".card-publisher").empty();
                $(".thumbnail").empty();
                $(".card-page").empty();
                $(".card-title").append("제목: " + msg.item[0].title);
                $(".card-author").append("저자: " + msg.item[0].author);
                $(".card-publisher").append("출판사: " + msg.item[0].publisher);
                $(".thumbnail").append('<img src="' + msg.item[0].cover + '"/>');
                $(".card-title2").empty();
                $(".card-author2").empty();
                $(".card-publisher2").empty();
                $(".thumbnail2").empty();
                $(".card-page2").empty();
                $(".card-title2").append("제목: " + msg.item[1].title);
                $(".card-author2").append("저자: " + msg.item[1].author);
                $(".card-publisher2").append("출판사: " +msg.item[1].publisher);
                $(".thumbnail2").append('<img src="' + msg.item[1].cover + '"/>');
                $("#button1").on('click', function () {
                    $('input[name=bookName]').attr('value', msg.item[0].title);
                    $('input[name=bookAuthor]').attr('value', msg.item[0].author);
                    $('input[name=bookPublisher]').attr('value', msg.item[0].publisher);
                    $('input[name=bookIsbn]').attr('value', msg.item[0].isbn13);
                });
                $("#button2").on('click', function () {
                    $('input[name=bookName]').attr('value', msg.item[1].title);
                    $('input[name=bookAuthor]').attr('value', msg.item[1].author);
                    $('input[name=bookPublisher]').attr('value', msg.item[1].publisher);
                    $('input[name=bookIsbn]').attr('value', msg.item[1].isbn13);
                });
            });
    });

});
// 엔터키 방지
document.myForm.addEventListener("keydown", evt => {
    if (evt.code === "Enter") evt.preventDefault();
});