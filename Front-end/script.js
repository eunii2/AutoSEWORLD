// 백엔드에서 데이터를 가져오는 함수
async function loadConferences() {
    try {
        const response = await fetch('http://localhost:8080/api/conference');
        const conferences = await response.json();
        
        const conferenceContainer = document.getElementById('conference-container');
        conferenceContainer.innerHTML = ''; 

        conferences.forEach(conference => {
            const card = document.createElement('div');
            card.className = 'conference-card';
            
            card.innerHTML = `
                <h3>${conference.conference_name}</h3>
                <p><strong>Submission Deadline  :</strong> ${conference.submission_deadline}</p>
                <p><strong>Location :</strong> ${conference.conference_location}</p>
                <p><strong>Dates :</strong> ${conference.conference_dates}</p>
                <p><strong>URL : </strong><a href="${conference.cfp_url}" target="_blank">${conference.cfp_url}</a></p>
            `;

            conferenceContainer.appendChild(card);
        });
    } catch (error) {
        console.error('Error fetching conference data:', error);
    }
}

window.onload = loadConferences;