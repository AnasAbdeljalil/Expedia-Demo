document.getElementById("searchForm").addEventListener("submit", function (event) {
    event.preventDefault();

    let originCity = document.getElementById("originCity").value;
    let destinationCity = document.getElementById("destinationCity").value;

    // Spinner
    let resultsDiv = document.getElementById("results");
    resultsDiv.innerHTML = '<div class="loader"></div>';

    fetch(`/api/offers?originCity=${originCity}&destinationCity=${destinationCity}`)
        .then(response => response.json())
        .then(data => {
            console.log("Received data:", data); // Debugging: Log the data

            resultsDiv.innerHTML = "<h2>Search Results:</h2>";

            // Check if data.offers exists and contains the Package array
            if (data && data.offers && data.offers.Package && Array.isArray(data.offers.Package)) {
                if (data.offers.Package.length > 0) {
                    console.log("offers:", data.offers);
                    console.log("Package:", data.offers.Package);
                    console.log("Package length:", data.offers.Package.length);
                    // If there are offers, display them
                    data.offers.Package.forEach(offer => {
                        resultsDiv.innerHTML += `
                            <div class="offer">
                                <h3>${offer.hotelInfo && offer.hotelInfo.hotelName || 'Unknown Hotel'}</h3>
                                <p><strong>Price:</strong> ${offer.packagePricingInfo && offer.packagePricingInfo.formattedTotalPriceValue || 'N/A'}</p>
                                <p><strong>Location:</strong> ${offer.destination && offer.destination.displayDestination || 'N/A'}</p>
                                <p><strong>Travel Dates:</strong> ${offer.offerDateRange && offer.offerDateRange.formattedTravelStartDate} to ${offer.offerDateRange && offer.offerDateRange.formattedTravelEndDate}</p>
                            </div>
                            <hr>
                        `;
                    });
                } else {
                    resultsDiv.innerHTML += "<h2>No package offers found.</h2>";
                }
            } else {
                resultsDiv.innerHTML += "<h2>No package offers found.</h2>";
            }
        })
        .catch(error => {
            console.error("Error fetching data:", error);
            resultsDiv.innerHTML = "<p>Error retrieving data.</p>";
        });
});