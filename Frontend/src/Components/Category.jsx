// Category.js
import React, { useState, useEffect } from 'react';
import '../styles/Category.css';
import { FaSearch } from 'react-icons/fa';
import { getAllCategories } from '../service/ApiService';

const Category = ({ handleCategoryClick }) => {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8080/v1/categories');
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        setCategories(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
  
    fetchData();
  }, []);

  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const categoriesList = await getAllCategories();
        setCategories(categoriesList);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  const filteredCategories = categories.filter((category) =>
    category.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleSearch = (e) => {
    e.preventDefault();
  };

  return (
    <div className='category-wrapper'>
      <div className='category-page'>
        <div className='search-box'>
          <form onSubmit={handleSearch}>
            {/* Add your search input here */}
          </form>
        </div>

        <div className='category-list'>
          {filteredCategories.length > 0 && <h2>Categories</h2>}
          <div className='category-cards'>
            {filteredCategories.length === 0 ? (
              <p className='no-matching-message'>
                There are no categories matching your search.
              </p>
            ) : (
              filteredCategories.map((category) => (
                <button
                  key={category.id}
                  className='category-card'
                  onClick={() => handleCategoryClick(category.id)}
                >
                  <h3>{category.name}</h3>
                  {/* Add more details or styling as needed */}
                </button>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Category;
