import React, { useState, useEffect } from 'react';
import '../styles/MyServicesModal.css';


const MyServicesModal = ({ isOpen, onClose, services }) => {
    if (!isOpen) return null;
  
    return (
      <div className="modal">
        <div className="modal-content">
          <span className="close" onClick={onClose}>&times;</span>
          <h2>My Services</h2>
          <ul>
            {services.map(service => (
              <li key={service.id}>{service.title}</li>
            ))}
          </ul>
        </div>
      </div>
    );
  };

  export default MyServicesModal;