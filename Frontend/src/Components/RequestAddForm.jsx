import React from 'react'
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { useState } from 'react';
import '../styles/RequestAddForm.css';

const RequestAddForm = ({ onAdd, serviceId }) => {
    const [requestDescription, setRequestDescription] = useState('');
    const navigate = useNavigate();
    const localToken = localStorage.getItem('Jwt_Token');
    if (!localToken) {
        console.error('No JWT token found');
        navigate('/sign-in');
        return;
    }
    const decodedToken = jwtDecode(localToken);
    const userId = decodedToken['jti'];
    const handleSubmit = (e) => {
        e.preventDefault();
        if (!requestDescription) {
            alert('Please add description');
            return;
        }
        if (requestDescription.length < 2) {
            alert('The description has to be more than 2 symbols');
            return;
        }
       
        
        const requestRequesst = {
            description: requestDescription,
            customerId: userId,
            serviceId: serviceId,
            isActive: 'PENDING'
        };
        onAdd(requestRequesst, false);
        setRequestDescription('');
    };

  return (
    <div className='request-add-form-container'>
            <hr />
            <h2 className="request-add-form-title">Add Request</h2>
            <form className="request-add-form" onSubmit={handleSubmit}>
                <div className="form-control">
                    <label>Description:</label>
                    <input
                        type="text"
                        className="request-description-input"
                        value={requestDescription}
                        onChange={(e) => setRequestDescription(e.target.value)}
                    />
                </div>
                <button className="submit-button" type="submit">Submit</button>
            </form>
            <hr className='bottom' />
        </div>
  )
}

export default RequestAddForm;