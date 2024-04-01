import React, { useState } from 'react';
import '../../styles/SignIn.css';
import { FaUserTie } from 'react-icons/fa';
import { IoIosLock } from 'react-icons/io';
import { Link, useNavigate } from 'react-router-dom';
import { GoogleLogin } from '@react-oauth/google';
import { googleLogin, postLogin } from '../../service/ApiService';
export const SignIn = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [error, setError] = useState('');
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };
  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await postLogin(formData);
      console.log('Backend response:', response);
      const jwtToken = response.token;
      localStorage.setItem('Jwt_Token', jwtToken);
      navigate('/home-page');
    } catch (error) {
      console.error('Error during login:', error);
      setError('Invalid email or password. Please try again.');
    }
  };
  const handleGoogleLogin = async (response) => {
    console.log(response.credential);
    try {
      console.log('Google Login Response:', response);
      const googleToken = response.credential
      console.log('Sending request to /api/auth/google/login:', googleToken);
      const backendResponse = await googleLogin(googleToken);
      console.log('Backend Response:', backendResponse);
      const jwtToken = backendResponse.token;
      localStorage.setItem('Jwt_Token', jwtToken);
      navigate('/home-page');
    } catch (error) {
      console.error('Error during Google login:', error);
    }
  };
  return (
    <div className='sign-in'>
      <div>
        <form onSubmit={handleFormSubmit}>
          <h1>Login</h1>
          <div className='input-box'>
            <input
              type='text'
              placeholder='Email'
              name='email'
              value={formData.email}
              onChange={handleChange}
              required
            />
            <FaUserTie className='icon' />
          </div>
          <div className='input-box'>
            <input
              type='password'
              placeholder='Password'
              name='password'
              value={formData.password}
              onChange={handleChange}
              required
            />
            <IoIosLock className='icon' />
          </div>
          {error && <p className='error-message'>{error}</p>}
          <div className='remember-forgot'>
            <label>
              <input type='checkbox' />Remember me
            </label>
            <a href="forget-password" >
              Forgotten password?
            </a>
          </div>
          <button type='submit'>Login</button>
          <div className='register-link'>
            <p>
              Don't have an account? <Link to='/sign-up'>Register</Link>
            </p>
          </div>
        </form>
        <div className='media-options'>
          <GoogleLogin
            onSuccess={handleGoogleLogin}
            onFailure={handleGoogleLogin}
            clientId="350761079008-0ipa8rk7sumieir1rq4b5ljg3pu78trt.apps.googleusercontent.com"
            buttonText="Login with Google"
            cookiePolicy={'single_host_origin'}
          />
        </div>
      </div>
    </div>
  );
};
export default SignIn;