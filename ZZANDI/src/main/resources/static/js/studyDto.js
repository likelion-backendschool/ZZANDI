

//달력
$("#studyStart").change(function () {
    const val = $(this).val();
    $("#studyEnd").attr("min", valg);
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
    var lectureName = document.getElementById("lectureName").value;
    var lecturer  = document.getElementById("lecturer").value;
    var lectureNumber = document.getElementById("lectureNumber").value;

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

    if($("input[name='studyType']:checked").val() == 'LECTURE') {

        if (lectureName == '') {
            alert("강의 제목을 입력해주십시오.");
            $('#lectureName').focus();
            return false;
        }

        if (lecturer == '') {
            alert("강사 이름을 입력해주십시오.");
            $('#lecturer').focus();
            return false;
        }

        if (lectureNumber == '') {
            alert("강의 개수를 입력해주십시오.");
            $('#lectureNumber').focus();
            return false;
        }
    }
});

//책 검색
var openWin;
function openSearch(){
    window.name="studyForm";
    openWin=window.open("/study/studyBookSearch","searchForm","width=750,height=600,resizable=no,scrollbars=no")
}

// 엔터키 방지
document.myForm.addEventListener("keydown", evt => {
    if (evt.code === "Enter") evt.preventDefault();
});