
export default function generateBabyURL(longUrl, setBabyUrlData) {
    console.log(longUrl)
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'text/plain' },
        body: longUrl
    };
    fetch(`http://localhost:8080/shortenURL`, requestOptions)
    .then(res => res.text())
    .then(babyUrl => {
        console.log(babyUrl)

        setBabyUrlData({longUrl: longUrl,babyUrl: babyUrl, isBabyUrlExists: true})
    })
}