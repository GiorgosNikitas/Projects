var count = 0;
var lastScroll = 0;

document.addEventListener('DOMContentLoaded', function () {
    const navbar = document.getElementById('navbar-top');
    const navbarIcon = document.getElementById('toggler');
    const navbarButton = document.getElementById('navbar-button');
    const links = navbar.getElementsByTagName('a');

    // navbar color
    window.addEventListener('scroll', function () {
        const scrollPosition = window.scrollY;
        lastScroll = scrollPosition;

        if (scrollPosition == 0) {
            navbar.style.backgroundColor = 'transparent';
            navbar.classList.remove('border-bottom');
            for (let i = 0; i < links.length; i++) {
                links[i].classList.remove('light');
                links[i].classList.add('dark');
            }
            navbarIcon.style.color = 'rgb(37, 37, 37)';
            navbarButton.style.borderColor = 'rgb(37, 37, 37)';
        } else {
            navbar.style.backgroundColor = 'rgb(37, 37, 37)';
            navbar.classList.add('border-bottom');
            for (let i = 0; i < links.length; i++) {
                links[i].classList.remove('dark');
                links[i].classList.add('light');
            }
            navbarIcon.style.color = 'rgb(179, 159, 159)';
            navbarButton.style.borderColor = 'rgb(179, 159, 159)';
        }
    });

    // small navbar collapse on click outside
    navbarButton.addEventListener('click', function () {
        count++;
        if (count % 2 == 1) {
            navbar.style.backgroundColor = 'rgb(37, 37, 37)';
            for (let i = 0; i < links.length; i++) {
                links[i].style.color = 'rgb(179, 159, 159)';
            }
            navbarIcon.style.color = 'rgb(179, 159, 159)';
            navbarButton.style.borderColor = 'rgb(179, 159, 159)';
            document.getElementById('helper-div').style.display = 'block';
        } else {
            if (lastScroll == 0) {
                navbar.style.backgroundColor = 'transparent';
                for (let i = 0; i < links.length; i++) {
                    links[i].style.color = 'rgb(37, 37, 37)';
                }
                navbarIcon.style.color = 'rgb(37, 37, 37)';
                navbarButton.style.borderColor = 'rgb(37, 37, 37)';
            } else {
                navbar.style.backgroundColor = 'rgb(37, 37, 37)';
                for (let i = 0; i < links.length; i++) {
                    links[i].style.color = 'rgb(179, 159, 159)';
                }
                navbarIcon.style.color = 'rgb(179, 159, 159)';
                navbarButton.style.borderColor = 'rgb(179, 159, 159)';
            }
            document.getElementById('helper-div').style.display = 'none';
        }
    });

    document.getElementById('helper-div').addEventListener('click', function () {
        navbarButton.click();
    });
});

// navbar hide on scroll
var prevScrollpos = window.pageYOffset;
window.onscroll = function () {
    var currentScrollPos = window.pageYOffset;
    if (prevScrollpos > currentScrollPos) {
        document.getElementById("navbar-top").style.top = "0";
    } else {
        document.getElementById("navbar-top").style.top = "-100px";
    }
    prevScrollpos = currentScrollPos;
}

const locationOptions = document.getElementsByClassName('location-option');

for (let i = 0; i < locationOptions.length; i++) {
    locationOptions[i].addEventListener('click', function () {
        locationOptions[i].classList.remove('not-selected');
        locationOptions[i].classList.add('selected');

        for (let j = 0; j < locationOptions.length; j++) {
            if (i != j) {
                locationOptions[j].classList.add('not-selected');
                locationOptions[j].classList.remove('selected');
            }
        }
    });
}


// const booking = document.getElementById('booking-img');
// const airbnb = document.getElementById('airbnb-img');

// function sleep(ms) {
//     return new Promise(resolve => setTimeout(resolve, ms));
// }

// booking.addEventListener('mouseover', async function () {
//     booking.style.opacity = 0;
//     await sleep(200);
//     booking.src = 'icons/booking-book.svg';
//     booking.style.opacity = 1;
// });

// booking.addEventListener('mouseout', async function () {
//     booking.style.opacity = 0;
//     await sleep(200);
//     booking.src = 'icons/booking-button.svg';
//     booking.style.opacity = 1;
// });

// airbnb.addEventListener('mouseover', async function () {
//     airbnb.style.opacity = 0;
//     await sleep(200);
//     airbnb.src = 'icons/airbnb-book.svg';
//     airbnb.style.opacity = 1;
// });

// airbnb.addEventListener('mouseout', async function () {
//     airbnb.style.opacity = 0;
//     await sleep(200);
//     airbnb.src = 'icons/airbnb-button.svg';
//     airbnb.style.opacity = 1;
// });

// Privacy Policy
document.getElementById('privacy-button').addEventListener('click', function () {
    document.getElementById('privacy-background').style.display = 'flex';
});

document.getElementById('privacy-close').addEventListener('click', function () {
    document.getElementById('privacy-background').style.display = 'none';
});

document.getElementById('privacy-background').addEventListener('click', function () {
    document.getElementById('privacy-background').style.display = 'none';
});

// document.getElementById('booking').addEventListener('click', function () {
//     location.href = 'https://www.booking.com/Share-ocH97L';
// });