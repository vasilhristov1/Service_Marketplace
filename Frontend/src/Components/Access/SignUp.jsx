import React, { useState } from 'react';
import '../../styles/SignUp.css';
import { FaUserPen } from 'react-icons/fa6';
import { IoIosLock } from 'react-icons/io';
import { MdOutlineEmail } from 'react-icons/md';
import { Link, useNavigate } from 'react-router-dom';
import { postRegister } from '../../service/ApiService';
const SignUp = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: ''
  });
  const [errorData, setErrorData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  });
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorData({
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    });
    let hasError = false;
    if (formData.firstName.trim() === '') {
      setErrorData((prevErrorData) => ({
        ...prevErrorData,
        firstName: 'First Name cannot be empty'
      }));
      hasError = true;
    }
    if (formData.lastName.trim() === '') {
      setErrorData((prevErrorData) => ({
        ...prevErrorData,
        lastName: 'Last Name cannot be empty'
      }));
      hasError = true;
    }
    if (!/\S+@\S+\.\S+/.test(formData.email)) {
      setErrorData((prevErrorData) => ({
        ...prevErrorData,
        email: 'Email is not valid'
      }));
      hasError = true;
    }
    if (formData.password !== formData.confirmPassword) {
      setErrorData((prevErrorData) => ({
        ...prevErrorData,
        password: 'Passwords do not match'
      }));
      hasError = true;
    }
    if (hasError) {
      return;
    }
    try {
      const response = await postRegister(formData);
      console.log('Backend response:', response.data);
      navigate('/sign-in');
    } catch (error) {
      console.error('Error during registration:', error);
    }
  };
  return (
    <div className='sign-up'>
      <form onSubmit={handleSubmit}>
        <h1>Registration</h1>
        <div className='input-box'>
          <input
            type='text'
            placeholder='First Name'
            name='firstName'
            value={formData.firstName}
            onChange={handleChange}
            required
          />
          <FaUserPen className='icon' />
          {errorData.firstName && <p className='error-message'>{errorData.firstName}</p>}
        </div>
        <div className='input-box'>
          <input
            type='text'
            placeholder='Last Name'
            name='lastName'
            value={formData.lastName}
            onChange={handleChange}
            required
          />
          <FaUserPen className='icon' />
          {errorData.lastName && <p className='error-message'>{errorData.lastName}</p>}
        </div>
        <div className='input-box'>
          <input
            type='text'
            placeholder='Email'
            name='email'
            value={formData.email}
            onChange={handleChange}
            required
          />
          <MdOutlineEmail className='icon' />
          {errorData.email && <p className='error-message'>{errorData.email}</p>}
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
          {errorData.password && <p className='error-message'>{errorData.password}</p>}
        </div>
        <div className='input-box'>
          <input
            type='password'
            placeholder='Confirm password'
            name='confirmPassword'
            value={formData.confirmPassword}
            onChange={handleChange}
            required
          />
          <IoIosLock className='icon' />
        </div>
        <div>
          <button type='submit'>Register</button>
          <div className='have-register'>
            <p>Already have an account? <Link to='/sign-in'>Login</Link></p>
          </div>
        </div>
      </form>
    </div>
  );
};
export default SignUp;
