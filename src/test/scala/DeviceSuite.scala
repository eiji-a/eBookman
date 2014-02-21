import org.scalatest.FunSuite

class DeviceSuite extends FunSuite {

  test("sample device") {
    val d1 = Device("sample", 100, 200, 100, Screen.EINK16, Device.LEFT)
    assert(d1.name === "sample")
    assert(d1.w === 100)
    assert(d1.h === 200)
    assert(d1.aspect === 2.0)
    assert(d1.dpi === 100)
    assert(d1.scr === Screen.EINK16)
    assert(d1.dir === Device.LEFT)
  }

  test("kindle3") {
    val d3 = Device.KINDLE3
    assert(d3.name === "kindle3")
    assert(d3.w === 600)
    assert(d3.h === 800)
    assert(d3.aspect === 800.0 / 600.0)
    assert(d3.scr === Screen.EINK16)
    assert(d3.dir === Device.LEFT)
  }

  test("kindle_pw") {
    val d3 = Device.KINDLE_PW
    assert(d3.name === "kindle_pw")
    assert(d3.w === 758)
    assert(d3.h === 1024)
    assert(d3.aspect === 1024.0 / 758.0)
    assert(d3.scr === Screen.EINK16)
    assert(d3.dir === Device.BOTH)
  }

  test("nexus7") {
    val d4 = Device.NEXUS7
    assert(d4.name === "nexus7")
    assert(d4.w === 800)
    assert(d4.h === 1205)
    assert(d4.aspect === 1205.0 / 800.0)
    assert(d4.scr === Screen.LCD256)
    assert(d4.dir === Device.BOTH)
  }

  test("ipad mini") {
    val d5 = Device.IPADMINI
    assert(d5.name === "ipad_mini")
    assert(d5.w === 768)
    assert(d5.h === 1008)
    assert(d5.aspect === 1008.0 / 768.0)
    assert(d5.scr === Screen.LCD256)
    assert(d5.dir === Device.BOTH)
  }

}

