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