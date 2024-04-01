import React from 'react';
import '../styles/ContactUsPage.css';

const ContactUsPage = () => {
  return (
    <div className="contact-us-container">
      <h1 className="contact-us-heading">Contact Us</h1>
      <div className="contact-list">
        <div className="contact-item">
          <h2 className="contact-item-heading">Service Marketplace Team</h2>
          <p className="contact-info">Email: <a href="mailto:service.marketplace.blankfactor@gmail.com" className="contact-link">service.marketplace.blankfactor@gmail.com</a></p>
          <p className="contact-info">Address: <br></br> Studentski Kompleks, bulevard „Doctor G. M. Dimitrov“ 59, 1756 Sofia</p>
        </div>
      </div>
    </div>
  );
};

export default ContactUsPage;
