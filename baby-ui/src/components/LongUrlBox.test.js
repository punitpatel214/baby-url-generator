import { fireEvent, render, screen } from "@testing-library/react"
import React from "react"
import LongUrlBox from "./LongUrlBox"

describe("LongUrlBox", () => {
  test("renders text box and button for make baby url", () => {
    render(<LongUrlBox generateBabyURL={() => {}} />)
    expect(screen.getByRole("button", { name: /Make Baby URL/ })).toBeInTheDocument()
    expect(screen.getByPlaceholderText("Enter the link here")).toBeInTheDocument()
  })

  test("generateBabyUrl function should invoke on button click", () => {
    const generateBabyURL = jest.fn()
    render(<LongUrlBox generateBabyURL={generateBabyURL} />)
    fireEvent.change(screen.getByRole("textbox"), {
      target: { value: "http://anylongurl.com" },
    })
    fireEvent.click(screen.getByRole("button", { name: /Make Baby URL/ }))
    expect(generateBabyURL).toHaveBeenCalledTimes(1)
  })
})
