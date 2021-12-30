import { useRef, React } from "react"
import PropTypes from "prop-types"

export default function LongUrlBox({ generateBabyURL }) {
  const longUrl = useRef()
  const makeBabyURL = (e) => {
    e.preventDefault()
    generateBabyURL(longUrl.current.value)
  }
  return (
    <section id="urlbox" data-testid="long-url-box">
      <h1>Enter the Long URL to make baby URL</h1>
      <form onSubmit={makeBabyURL}>
        <div id="formurl">
          <input type="url" pattern="https?://.+" name="u" placeholder="Enter the link here" ref={longUrl} required />
          <div id="formbutton">
            <input type="submit" value="Make Baby URL"></input>
          </div>
        </div>
      </form>
      <p className="boxtextcenter">
        baby url is a free tool to shorten a URL or reduce a link
        <br />
      </p>
    </section>
  )
}

LongUrlBox.propTypes = {
  generateBabyURL: PropTypes.func.isRequired,
}
