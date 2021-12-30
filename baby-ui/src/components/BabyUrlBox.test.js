import { fireEvent, render, screen } from "@testing-library/react"
import React from "react"
import BabyUrlBox from "./BabyUrlBox"

describe("BabyURLBox", () => {
  test("renders baby URL box", () => {
    const resetBabyUrl = jest.fn()
    render(<BabyUrlBox babyUrl="babyUrl" longUrl="longUrl" resetBabyUrl={resetBabyUrl} />)
    expect(screen.getByDisplayValue("babyUrl")).toBeInTheDocument()
    expect(screen.getByRole("button", { name: /COPY URL/ })).toBeInTheDocument()
    expect(screen.getByRole("link", { name: /longUrl/ })).toBeInTheDocument()
    expect(screen.getByRole("link", { name: /baby URL/ })).toBeInTheDocument()
    expect(resetBabyUrl).toHaveBeenCalledTimes(0)
  })

  test("should invoke reset baby url function when click on link", () => {
    const resetBabyUrl = jest.fn()
    render(<BabyUrlBox babyUrl="babyUrl" longUrl="longUrl" resetBabyUrl={resetBabyUrl} />)
    fireEvent.click(screen.getByRole("link", { name: /baby URL/ }))
    expect(resetBabyUrl).toHaveBeenCalledTimes(1)
  })

  test("should copy baby url when click on copy url button", async () => {
    const useStateMock = (useState) => [true, jest.fn()]
    jest.spyOn(React, "useState").mockImplementation(useStateMock)
    const resetBabyUrl = jest.fn()
    render(<BabyUrlBox babyUrl="babyUrl" longUrl="longUrl" resetBabyUrl={resetBabyUrl} />)
    const mockClipBoard = {
      writeText: jest.fn(),
    }
    global.navigator.clipboard = mockClipBoard
    fireEvent.click(screen.getByRole("button", { name: /COPY URL/ }))
    expect(mockClipBoard.writeText).toHaveBeenCalledTimes(1)
    expect(screen.getByText(/URL Copied/)).toBeInTheDocument()
  })
})
