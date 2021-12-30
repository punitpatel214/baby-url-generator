import { useState, React } from "react"
import PropTypes from "prop-types"

export default function BabyUrlBox({ babyUrl, longUrl, resetBabyUrl }) {
  const [isCopiedToClipboard, setCopiedToClipboard] = useState(false)

  const copyToClipboard = async (text) => {
    await navigator.clipboard.writeText(text)
    setCopiedToClipboard(true)
  }

  return (
    <section id="urlbox" data-testid="baby-url-box">
      <div id="formurl">
        <input type="text" readOnly={true} value={babyUrl} />
        <div id="formbutton">
          <input type="button" value="COPY URL" onClick={() => copyToClipboard(babyUrl)} />
        </div>
      </div>
      {isCopiedToClipboard && <DisplayURLCopyMessage showMessage={setCopiedToClipboard} />}
      <p className="boxtextleft">
        Long URL:{" "}
        <a href={longUrl} rel="noreferrer" target="_blank">
          {longUrl}
        </a>
        <br />
        <br />
        Create other{" "}
        <a href="#" onClick={resetBabyUrl}>
          baby URL
        </a>
        .
      </p>
    </section>
  )
}

const DisplayURLCopyMessage = ({ showMessage }) => {
  setTimeout(() => showMessage(false), 3000)
  return (
    <div id="formurl" style={{ maxWidth: "400px", display: "block" }}>
      <div id="balloon" style={{ display: "table" }}>
        URL Copied
      </div>
    </div>
  )
}

DisplayURLCopyMessage.propTypes = {
  showMessage: PropTypes.func.isRequired,
}

BabyUrlBox.propTypes = {
  babyUrl: PropTypes.string.isRequired,
  longUrl: PropTypes.string.isRequired,
  resetBabyUrl: PropTypes.func.isRequired,
}
