import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '1m', target: 50},
        { duration: '2m', target: 100},
        { duration: '30s', target: 100},
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        http_req_failed: ['rate<0.01'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://fixit-backend-service';

export default function () {
    const res = http.get(`${BASE_URL}/fixit-backend/api/v1/ordem-servico`);

    check(res, {
        'status 2xx': (r) => r.status >= 200 && r.status < 300,
        'tempo aceitável': (r) => r.timings.duration < 500,
    });

}
