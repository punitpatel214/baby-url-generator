import React, { useState } from "react"
import LongUrlBox from "./LongUrlBox"
import BabyUrlBox from "./BabyUrlBox"
import generateBabyURL from "./GenerateBabyURLHandler"
import PropTypes from "prop-types"

export default function BabyUrlContainer({ babyUrlData = {} }) {
  const [babyUrlDetail, setBabyUrlData] = useState(babyUrlData)

  const resetBabyUrl = () => setBabyUrlData({})

  if (babyUrlDetail.babyUrl) {
    return <BabyUrlBox longUrl={babyUrlDetail.longUrl} babyUrl={babyUrlDetail.babyUrl} resetBabyUrl={resetBabyUrl} />
  }

  return <LongUrlBox generateBabyURL={(longUrl) => generateBabyURL(longUrl, setBabyUrlData)} />
}

BabyUrlContainer.propTypes = {
  babyUrlData: PropTypes.object,
}
