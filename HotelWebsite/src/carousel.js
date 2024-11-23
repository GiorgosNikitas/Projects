const prev = document.getElementById('prev-btn')
const next = document.getElementById('next-btn')
const list = document.getElementById('item-list')
const swipervar = document.getElementsByClassName('swiper-wrapper')[0]

var screenWidth = window.innerWidth;
var screenLength = window.innerHeight;

var carouselIsFull = false;

var mutliplier = 0.4;

if (screenWidth < 500) {
    mutliplier = 0.8;
} else if (screenWidth < 768) {
    mutliplier = 0.7;
} else if (screenWidth < 1000) {
    mutliplier = 0.5;
} else {
    mutliplier = 0.4;
}

const itemWidth = screenWidth * mutliplier;
const padding = 0;
const url = 'https://ik.imagekit.io/y8mlo5u0s3/';

const totalPhotos = 29;

var imageArray = [];
for (let i = 1; i < totalPhotos; i++) {
    imageArray.push(url + `photos/photo_${totalPhotos}.jpg`);
}

function preloadImages() {
    imageArray.forEach((src) => {
        const img = new Image();
        img.src = src;
        img.loading = 'lazy';
    });
}

preloadImages();

function initCarousel() {
    for (let i = 1; i <= totalPhotos; i++) {
        if (i == 1) {
            let img = document.createElement('img');
            img.id = totalPhotos-1;
            img.className = 'item';
            img.src = url + `photos/photo_${totalPhotos}.jpg`;
            list.appendChild(img);
        }
        let img = document.createElement('img');
        img.id = i-1;
        img.className = 'item';
        img.src = url + `photos/photo_${i}.jpg`;

        img.onclick = () => {
            showCarousel(img.id);
        }

        list.appendChild(img);
        if (i == totalPhotos) {
            let img = document.createElement('img');
            img.id = 0;
            img.className = 'item';
            img.src = url + `photos/photo_1.jpg`;
            list.appendChild(img);
        }
    }
}

function initCarouselFull() {
    for (let i = 1; i <= totalPhotos; i++) {
        let img = document.createElement('img');
        img.src = url + `photos/photo_${i}.jpg`;
        img.id = i-1;

        let zoom = document.createElement('div');
        zoom.classList.add('swiper-zoom-container');

        let slide = document.createElement('div');
        slide.classList.add('swiper-slide');
        zoom.appendChild(img);
        
        slide.appendChild(zoom);

        swipervar.appendChild(slide);
    }
}

function showCarousel(id) {
    document.getElementById("carousel-full").style.display = 'flex';
    document.getElementById("helper-div-carousel").style.display = 'flex';
    carouselIsFull = true;
    swiper.slideTo(id);
    document.body.classList.add("stop-scrolling");
}

function hideCarousel() {
    document.getElementById("carousel-full").style.display = 'none';
    document.getElementById("helper-div-carousel").style.display = 'none';
    carouselIsFull = false;
    document.body.classList.remove("stop-scrolling");
}

document.getElementById("helper-div-carousel").onclick = () => {
    hideCarousel();
}

document.getElementById('close-btn').onclick = () => {
    hideCarousel();
}

document.addEventListener('DOMContentLoaded', () => {
    initCarousel();

    list.scrollLeft += itemWidth / 2;

    initCarouselFull();
});

let currentIndex = 1;

prev.addEventListener('click', () => {
    currentIndex--;
    if (currentIndex < 1) {
        list.scrollLeft = totalPhotos * itemWidth - itemWidth / 2;
        currentIndex = totalPhotos;
    } else {
        list.scrollLeft -= itemWidth + padding
    }
})

next.addEventListener('click', () => {
    currentIndex++;
    if (currentIndex > 29) {
        list.scrollLeft = 0 + itemWidth / 2;
        currentIndex = 1;
    } else {
        list.scrollLeft += itemWidth + padding
    }
})