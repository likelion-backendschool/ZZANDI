'use strict';

document.querySelector('#todo-btn').addEventListener('click', () => {
    const iframe = document.querySelector('#todo-list');
    if(iframe.dataset.type === 'hidden') {
        iframe.setAttribute('src', iframe.dataset.src);
        iframe.dataset.type = 'visible';
        iframe.style.display = 'inline';
    } else {
        iframe.setAttribute('src', '');
        iframe.dataset.type = 'hidden';
        iframe.style.display = 'none';
    }
});