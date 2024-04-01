// Footer.jsx
import React from 'react';
import '../styles/Footer.css';
import { Link } from 'react-router-dom';

const Footer = () => {
  return (
    <>
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />

      {/* GOOGLE FONTS */}
      <link rel="preconnect" href="https://fonts.gstatic.com" />
      <link href="https://fonts.googleapis.com/css2?family=Fredoka+One&family=Play&display=swap" rel="stylesheet" />

      <footer>
        <div className="footer">
          <div className="row">
            <a href="#"><i className="fa fa-facebook"></i></a>
            <a href="#"><i className="fa fa-instagram"></i></a>
            <a href="#"><i className="fa fa-youtube"></i></a>
            <a href="#"><i className="fa fa-twitter"></i></a>
          </div>

          <div className="row">
            <ul>
              <li><Link to="/contact-us">Contact us</Link></li>
              <li><a href="services">Our Services</a></li>
              <li><a href="#">Privacy Policy</a></li>
              <li><a href="#">Terms & Conditions</a></li>

            </ul>
          </div>

          <div className="row">
            Blankfactor Copyright © 2024 Blankfactor - All rights reserved
          </div>
        </div>
      </footer>
    </>
  );
};

export default Footer;
