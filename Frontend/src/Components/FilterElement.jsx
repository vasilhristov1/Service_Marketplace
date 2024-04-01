import React from 'react';
import Multiselect from 'multiselect-react-dropdown';

const FilterElement = ({ title, data, chosen, setChosen}) => {
  return (
    <div>
          <label>{title}</label>
          <Multiselect
            options={data}
            selectedValues={chosen}
            onSelect={(selectedList) => setChosen(selectedList)}
            onRemove={(selectedList) => setChosen(selectedList)}
            displayValue='name'
          />
    </div>
  )
}

export default FilterElement;
