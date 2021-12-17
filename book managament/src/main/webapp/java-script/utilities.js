export function myFetch(url, fetchData, callback) {
    fetch(url, fetchData)
      .then(res => res.json())
      .then(
        (result) => {
          callback(result);
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          //catch err
        }
      )
  }
