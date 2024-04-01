import React, { useState } from 'react';
import { resetPassword } from '../service/ApiService';
import { useNavigate } from 'react-router-dom';
import '../styles/ResetPassword.css';

const ResetPassword = () => {
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordMatchError, setPasswordMatchError] = useState('');
  const navigate = useNavigate();

  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const token = urlParams.get('token');

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!password) {
      alert('Please write your new password');
      return;
    }

    if (password.length < 6) {
      alert('The password has to be more than 6 symbols');
      return;
    }

    if (!confirmPassword) {
      alert('Please confirm your new password');
      return;
    }

    if (confirmPassword !== password) {
      alert('Please enter matching passwords');
      setPasswordMatchError('Passwords do not match!');
      return;
    }

    const forgetPasswordRequest = {
      token: token,
      password: password
    }

    try {
      await resetPassword(forgetPasswordRequest);
      alert('Password reset successfully.');
      navigate('/sign-in');
    } catch (error) {
      alert('Failed to reset password: ' + error.response.data);
    }

    setPassword('');
    setConfirmPassword('');
  };

  return (
    <div className="reset-password-container">
      <h2>Reset Your Password</h2>

      <form onSubmit={handleSubmit} className="reset-password-form">
        <input type="hidden" name="token" value={token} />
        <div className="reset-password-inputs border border-secondary rounded p-3">
          <div>
            <p>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="form-control"
                placeholder="Enter your new password"
                required
                autoFocus
              />
            </p>
            <p>
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="form-control"
                placeholder="Confirm your new password"
                required
              />
            </p>
            {passwordMatchError && <p className="text-danger">{passwordMatchError}</p>}
            <p className="text-center">
              <input type="submit" value="Change Password" className="btn btn-primary" />
            </p>
          </div>
        </div>
      </form>
    </div>
  );
};

export default ResetPassword;
