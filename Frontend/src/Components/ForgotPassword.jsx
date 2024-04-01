import React, { useState } from 'react';
import { forgotPassword } from '../service/ApiService';
import '../styles/ForgotPassword.css';

const ForgotPassword = () => {
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const [email, setEmail] = useState('');

    const handleFormSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await forgotPassword(email);
            setMessage(response);
            setError(null);
        } catch (error) {
            setMessage(null);
            setError(error.response.data);
        }
    };

    return (
        <div className="forgot-password-container">
            <h2 className="forgot-password-title">Forgot Password</h2>

            {error && (
                <div className="error-container">
                    <p className="error-message">{error}</p>
                </div>
            )}

            {message && (
                <div className="message-container">
                    <p className="message">{message}</p>
                </div>
            )}

            <form onSubmit={handleFormSubmit} className="forgot-password-form">
                <div className="form-section">
                    <p className="form-description">We will be sending a reset password link to your email.</p>
                </div>
                <div className="form-section">
                    <input
                        type="email"
                        name="email"
                        className="email-input"
                        placeholder="Enter your e-mail"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        autoFocus
                    />
                </div>
                <div className="form-section">
                    <input type="submit" value="Send" className="submit-button" />
                </div>
            </form>
        </div>
    );
};

export default ForgotPassword;
