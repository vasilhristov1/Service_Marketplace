const isTokenExpired = (token) => {
    if (!token) {
      return true;
    }
  
    const decodedToken = decodeToken(token);
  
    if (!decodedToken || !decodedToken.exp) {
      return true;
    }
  
    const currentTime = Math.floor(Date.now() / 1000);
    return currentTime >= decodedToken.exp;
  };
  
  const decodeToken = (token) => {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload;
    } catch (error) {
      console.error("Error decoding token:", error);
      return null;
    }
  };

  export default isTokenExpired;