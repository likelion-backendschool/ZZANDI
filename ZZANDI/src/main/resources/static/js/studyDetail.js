$(document).ready(function () {
    $('#book').hide();
    $('#lecture').hide();
});

function dis() {
    if ($('#book').css('display') == 'none') {
        $('#book').show();
    } else {
        $('#book').hide();
    }

    if ($('#lecture').css('display') == 'none') {
        $('#lecture').show();
    } else {
        $('#lecture').hide();
    }
}

function delegate() {
    let delegates = document.querySelectorAll('.delegate');
    delegates.forEach(delegate => {
        delegate.style.display = 'inline';
    });
}

// 진도율 관련 js 부분 (시작)
// ZZANDI 부분
// Controller 의 model 로 부터 값을 받아옴
const zzandi = document.querySelectorAll('.zzandi');
const studyperiod = document.getElementById('studyperiod').value;
const studydays = document.getElementById('studydays').value;

function calcRate(studyperiod, studydays) {
    return (studydays / studyperiod) * 100;
}

zzandi.forEach((zzandi, index) => {
    const Width = calcRate(studyperiod, studydays);
    console.log(Width);
    zzandi.style.width = `${Width}%`;
});

// 개인 유저 부분
function toggleStudyInput() {
    const study_input = document.getElementById("study_input");

    if (study_input.style.display !== "none") {
        study_input.style.display = "none";
    }
    else {
        study_input.style.display = "inline";
    }
}

const bars = document.querySelectorAll('.bar');
const progress = document.querySelectorAll('.progress');

bars.forEach((bar, index) => {
    const randomWidth = Math.floor((Math.random() * 65) + 10);
    bar.style.width = `${randomWidth}%`;

    // progress[index].addEventListener('mouseover', () => {
    //   const randomTiming = Math.floor((Math.random() * 2) + 2);
    //   console.log(randomTiming);
    //   bar.style.transitionDuration = `${randomTiming}s`;
    //   bar.style.width = '100%';
    // });
})
// 진도율 관련 js 부분 (끝)