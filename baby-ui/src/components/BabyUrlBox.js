import { useState } from "react";

export default function BabyUrlBox({ babyUrl, longUrl, resetBabyUrl }) {
    const [isCopied, setIsCopied] = useState(false);
    const resetCopy = () => setIsCopied(false);
    if (isCopied) {
        setTimeout(resetCopy, 5000)
    }
    return (
        <section id="urlbox">
            <div id="formurl">
                <input type="text" readOnly={true} value={babyUrl} />
                <div id="formbutton"><input type="button" value="COPY URL" onClick={() => copyToClipboard(babyUrl, setIsCopied)} /></div>
            </div>
            {isCopied && <UrlCopied />}
            <p className="boxtextleft">
                Long URL: <a href={longUrl} target="_blank">{longUrl}</a>
                <br /><br />
                Create other <a href="#" onClick={resetBabyUrl}>baby URL</a>.
            </p>
        </section>
    );
}

const UrlCopied = () => {
    return (
        <div id="formurl" style={{ maxWidth: "400px", display: "block" }}>
            <div id="balloon" style={{ display: "table" }}>URL Copied</div>
        </div>
    )
}

const copyToClipboard = async (text, setIsCopied) => {
    await navigator.clipboard.writeText(text);
    setIsCopied(true);
}