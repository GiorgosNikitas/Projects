var swiper = new Swiper("#swiper-full", {
  slidesPerView: 1,
  spaceBetween: 30,
  // loop: true,
  zoom: {
    maxRatio: 5, // Maximum zoom level
  },
  pagination: {
    el: ".swiper-pagination",
    type: "fraction",
  },
  navigation: {
    nextEl: ".swiper-button-next",
    prevEl: ".swiper-button-prev",
  },
  lazy: true, // Enable lazy loading
  preloadImages: false, // Disable automatic preloading
});