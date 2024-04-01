import React from 'react';
import '../styles/Home.css';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import Category from './Category';
import Footer from './Footer';
import firstPhoto from '../assets/istockphoto-1127929107-612x612.jpg'
import secondPhoto from '../assets/photodune-6221194-customer-service-team-s-e1468394369939-742x353.jpg';
import thirdPhoto from '../assets/social-media-banner-grm.jpg';
import fourthphoto from '../assets/depositphotos_142638849-stock-illustration-place-your-ads-here-rubber.jpg';

const Home = () => {
  const images = [
    firstPhoto,
    secondPhoto,
    thirdPhoto,
    fourthphoto
  ];

  const carouselSettings = {
    showThumbs: false,
    interval: 3000,
    infiniteLoop: true,
    autoPlay: true,
    transitionTime: 600,
    stopOnHover: false,
    dynamicHeight: false,
  };

  const handleCategoryClick = (categoryId) => {
    sessionStorage.setItem('CategoryFilter', categoryId);
    window.location.href = '/services';
  };

  return (
    <div>
      <Carousel {...carouselSettings}>
        {images.map((imageUrl, index) => (
          <div key={index}>
            <img src={imageUrl} alt={`Photo ${index + 1}`} />
          </div>
        ))}
      </Carousel>
      <hr className='line-home'/>
      <div className="home-category">
        <Category handleCategoryClick={handleCategoryClick} />
      </div>
      <Footer></Footer>
    </div>
  );
};

export default Home;
