'use strict';

// 토스트 UI 뷰어
const Editor = toastui.Editor;
const viewer = new Editor.factory({
    el: document.querySelector('#viewer'),
    initialValue: document.querySelector(".board-content").value,
    viewer: true,
    plugins: [Editor.plugin.codeSyntaxHighlight]
});

function deleteBoard() {
    if (confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }
    document.forms['form'].submit();
}