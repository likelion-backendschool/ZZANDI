'use strict';

// 토스트 UI 뷰어
const Editor = toastui.Editor;
const viewer = new Editor.factory({
    el: document.querySelector('#viewer'),
    initialValue: document.querySelector(".board-content").value,
    viewer: true,
    plugins: [Editor.plugin.codeSyntaxHighlight],
    customHTMLRenderer: {
        htmlBlock: {
            iframe(node) {
                const link = node.attrs.src;
                return [
                    {
                        type: 'openTag',
                        tagName: 'iframe',
                        outerNewLine: true,
                        classNames: ['youtube'],
                        attributes: {
                            src: link
                        },
                    },
                    {
                        type: 'html',
                        content: node.childrenHTML
                    },
                    {
                        type: 'closeTag',
                        tagName: 'iframe',
                        outerNewLine: true
                    },
                ];
            },
        },
    },
});


function deleteBoard() {
    if (confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }
    document.forms['form'].submit();
}

function deleteFile(studyId, fileId) {
    if(confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }

    fetch(`/${studyId}/board/delete/file/${fileId}`, {
        method: 'POST'
    }).then(() => {
        console.log("첨부 파일 화면 체인지!!");
    });
}