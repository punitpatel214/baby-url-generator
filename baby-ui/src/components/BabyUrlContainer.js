import React, { useState } from "react";
import LongUrlBox from "./LongUrlBox";
import BabyUrlBox from "./BabyUrlBox";
import generateBabyURL from "./GenerateBabyURLHandler";


export default function BabyUrlContainer() {
    const [babyUrlDetail, setBabyUrlData] = useState(createBabyUrlData)

    const generateBabyUrlHandler = (longUrl) => generateBabyURL(longUrl, setBabyUrlData);

    const resetBabyUrl = () => setBabyUrlData(createBabyUrlData);

    if (babyUrlDetail.isBabyUrlExists) {
        return (
            <BabyUrlBox longUrl={babyUrlDetail.longUrl} babyUrl={babyUrlDetail.babyUrl} resetBabyUrl={resetBabyUrl} />
        );
    }

    return (
        <LongUrlBox generateBabyURL={generateBabyUrlHandler} />
    );

}

const createBabyUrlData = (longUrl = "", babyUrl = "") => {
    return {
        longUrl: longUrl,
        babyUrl: babyUrl,
        isBabyUrlExists: babyUrl.length > 0
    }
}

