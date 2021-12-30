import { render, screen } from "@testing-library/react"
import React from "react"
import BabyUrlContainer from "./BabyUrlContainer"

describe("BabyURLContainer", () => {
  test("renders long url box when baby url is empty", () => {
    render(<BabyUrlContainer />)
    expect(screen.getByTestId("long-url-box")).toBeInTheDocument()
    expect(screen.queryByTestId(/baby-url-box/)).not.toBeInTheDocument()
  })

  test("rende baby url box when baby-url is not empty", () => {
    const babyUrlData = {
      babyUrl: "babyurllink",
      longUrl: "longUrl",
    }
    render(<BabyUrlContainer babyUrlData={babyUrlData} />)
    expect(screen.getByTestId("baby-url-box")).toBeInTheDocument()
    expect(screen.queryByTestId(/long-url-box/)).not.toBeInTheDocument()
  })
})
