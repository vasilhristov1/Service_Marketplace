import PropTypes from 'prop-types';

const ServicesPageHeader = ({ title }) => {
  return (
    <header className='header'>
      <h1 >{ title }</h1>
    </header>
  )
}

ServicesPageHeader.defaultProps = {
    title: 'Services',
}

ServicesPageHeader.propTypes = {
    title: PropTypes.string.isRequired
}

export default ServicesPageHeader
