// 지도 초기화 함수
function initMap(latitude = 33.450701, longitude = 126.570667) {
    const container = document.getElementById('map'); // 지도를 표시할 div
    const options = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 3
    };
    const map = new kakao.maps.Map(container, options);

    return map; // 맵 객체를 반환
}

// 마커를 추가하는 함수
function addMarker(map, latitude, longitude) {
    const markerPosition  = new kakao.maps.LatLng(latitude, longitude);
    const marker = new kakao.maps.Marker({
        position: markerPosition
    });
    marker.setMap(map); // 마커를 지도에 추가
}

// 백엔드에서 데이터를 불러오기 위한 함수
async function loadConferences() {
    try {
        const response = await fetch('http://localhost:8080/api/conference');
        // 서버 응답이 성공적인지 확인
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const conferences = await response.json(); // fetch로 받은 데이터를 JSON으로 변환합니다.

        const tableBody = document.getElementById('conference-table-body');
        tableBody.innerHTML = '';  // 테이블을 비웁니다.

        // 기본 지도 로드
        const map = initMap();

        // 데이터를 테이블에 추가하고, 각 열에 클릭 이벤트를 추가
        conferences.forEach(conference => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td><a href="#" class="conference-name-link">${conference.conference_name}</a></td>
            `;
            // 클릭 이벤트 추가: 클릭 시 오른쪽 패널에 정보 표시 및 지도 위치 업데이트
            row.querySelector('.conference-name-link').addEventListener('click', (event) => {
                event.preventDefault(); // 기본 하이퍼링크 동작을 막습니다.
                document.getElementById('detail-name').textContent = conference.conference_name;
                document.getElementById('detail-deadline').textContent = conference.submission_deadline;
                document.getElementById('detail-url').innerHTML = `<a href="${conference.cfp_url}" target="_blank">${conference.cfp_url}</a>`;
                document.getElementById('detail-location').textContent = conference.conference_location;
                document.getElementById('detail-dates').textContent = conference.conference_dates;

                // 지도 위치 업데이트
                const geocoder = new kakao.maps.services.Geocoder();
                geocoder.addressSearch(conference.conference_location, (result, status) => {
                    if (status === kakao.maps.services.Status.OK) {
                        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                        map.setCenter(coords); // 지도의 중심을 변경
                        addMarker(map, result[0].y, result[0].x); // 마커 추가
                    } else {
                        console.error("Geocoding failed: ", status);
                    }
                });
            });

            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error('Error fetching conference data:', error);
    }
}

// 페이지 로드 시 데이터를 가져옵니다.
window.onload = loadConferences;