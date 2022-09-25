'use strict';

const closeBtn = document.querySelector('.close-btn');
const todoBtn = document.querySelector('#todo-btn');
document.querySelector('#todo-btn').addEventListener('click', () => {
    const iframe = document.querySelector('#todo-list');
    iframe.setAttribute('src', iframe.dataset.src);
    iframe.dataset.type = 'visible';
    iframe.style.display = 'inline';
    closeBtn.style.display = 'inline';
    todoBtn.style.opacity = 0;
});

closeBtn.addEventListener('click', () => {
    const iframe = document.querySelector('#todo-list');
    iframe.style.display = 'none';
    iframe.setAttribute('src', '');
    closeBtn.style.display = 'none';
    todoBtn.style.opacity = 1;
})