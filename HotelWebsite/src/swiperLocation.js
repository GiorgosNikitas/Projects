const paginationWords = ['Beaches', 'Location', 'Landmarks'];

var swiperLocation = new Swiper("#swiper-location", {
    autoHeight: true,
    direction: "horizontal",
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
        renderBullet: function (index, className) {
          return '<span class="' + className + '">' + paginationWords[index] + '</span>';
        },
    }
});

swiperLocation.slideTo(1);