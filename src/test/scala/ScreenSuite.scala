import org.scalatest.FunSuite

class ScreenSuite extends FunSuite {

  test("params") {
    assert(Screen.COLOR === true)
    assert(Screen.GRAY === false)

    assert(Screen.EINK16.kind === "EInk")
    assert(Screen.EINK16.tone === 16)
    assert(Screen.EINK16.color === false)

    assert(Screen.LCD256.kind === "LCD")
    assert(Screen.LCD256.tone === 256)
    assert(Screen.LCD256.color === true)

  }

}
