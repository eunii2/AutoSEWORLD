// script.js

// 백엔드에서 데이터를 불러오기 위한 함수
async function loadConferences() {
    try {
        const response = await fetch('http://localhost:8080/api/conference');
        const conferences = await response.json();

        const tableBody = document.getElementById('conference-table-body');
        tableBody.innerHTML = '';  // 테이블을 비웁니다.

        // 데이터를 테이블에 추가
        conferences.forEach(conference => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${conference.conference_name}</td>
                <td>${conference.submission_deadline}</td>
                <td><a href="${conference.cfp_url}" target="_blank">${conference.cfp_url}</a></td>
                <td>${conference.conference_location}</td>
                <td>${conference.conference_dates}</td>
            `;
            tableBody.appendChild(row);
        });
        
    } catch (error) {
        console.error('Error fetching conference data:', error);
    }
}

// 페이지 로드 시 데이터를 가져옵니다.
window.onload = loadConferences;
