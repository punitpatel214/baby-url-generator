import { API } from "../../settings/constants"

export default function generateBabyURL(longUrl, setBabyUrlData) {
  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url: longUrl }),
  }
  fetch(API.shortenURL, requestOptions)
    .then((res) => res.json())
    .then((res) => setBabyUrlData({ longUrl: longUrl, babyUrl: res.babyURL }))
}
