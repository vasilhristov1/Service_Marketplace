import React from 'react';

const Service = () => {
    const services = [
        { id: 1, name: 'Painter' },
        { id: 2, name: 'Wheel Repair' },
        { id: 3, name: 'TV Repair' },
        { id: 4, name: 'Computer Repair' },
        { id: 5, name: 'Computer Test 5' },
        { id: 6, name: 'Test Wheel' },
        { id: 7, name: 'Painter Wheel Test' },
        { id: 8, name: 'Category 8' },
        { id: 9, name: 'Category 9' },
        { id: 10, name: 'Category 10' },
      ];
    
      // const [searchQuery, setSearchQuery] = useState('');
    
    //   const filteredCategories = categories.filter((category) =>
    //     category.name.toLowerCase().includes(searchQuery.toLowerCase())
    //   );
    
      const handleSearch = (e) => {
        e.preventDefault();
      };
    
      return (
        <div className='category-wrapper'>
          <div className='category-page'>
            <div className='category-list'>
              {filteredCategories.length > 0 && <h2>Services</h2>}
              <div className='category-cards'>
                {filteredCategories.length === 0 ? (
                  <p className='no-matching-message'>
                    There are no categories matching your search.
                  </p>
                ) : (
                  filteredCategories.map((category) => (
                    <a key={category.id} href={`#${category.name}`} className='category-card'>
                      <h3>{category.name}</h3>
                      <p>{category.description}</p>
                    </a>
                  ))
                )}
              </div>
            </div>
          </div>
        </div>
      );
}

export default Service
