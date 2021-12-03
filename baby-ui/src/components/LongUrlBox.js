import { useRef } from "react";

export default function LongUrlBox({ generateBabyURL }) {
    const longUrl = useRef();
    return (
        <section id="urlbox">
            <h1>Enter the Long URL to make baby URL</h1>

            <div id="formurl">
                <input type="text" name="u"  placeholder="Enter the link here" ref={longUrl} />
                <div id="formbutton">
                    <input type="button" onClick={() => generateBabyURL(longUrl.current.value)} value="Make Baby URL"></input>
                </div>
            </div>
            <p className="boxtextcenter">
                baby url is a free tool to shorten a URL or reduce a link<br />
            </p>
        </section>
    );
}