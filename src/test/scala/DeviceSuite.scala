import org.scalatest.FunSuite

class DeviceSuite extends FunSuite {

  test("sample device") {
    val d1 = Device("sample", 100, 200, "gray16")
    assert(d1.name === "sample")
    assert(d1.w === 100)
    assert(d1.h === 200)
    assert(d1.aspect === 2.0)
    assert(d1.col === "gray16")
  }

  test("sample with default color") {
    val d2 = Device("sample2", 150, 200)
    assert(d2.name === "sample2")
    assert(d2.w === 150)
    assert(d2.h === 200)
    assert(d2.aspect === 200.0 / 150.0)
    assert(d2.col === "gray")
  }    

  test("kindle3") {
    val d3 = Device.KINDLE3
    assert(d3.name === "kindle3")
    assert(d3.w === 560)
    assert(d3.h === 734)
    assert(d3.aspect === 734.0 / 560.0)
    assert(d3.col === "gray")
  }

  test("readert1") {
    val d4 = Device.READERT1
    assert(d4.name === "reader_t1")
    assert(d4.w === 584)
    assert(d4.h === 754)
    assert(d4.aspect === 754.0 / 584.0)
    assert(d4.col === "gray")
  }

  test("ipad") {
    val d5 = Device.IPAD
    assert(d5.name === "ipad")
    assert(d5.w === 768)
    assert(d5.h === 1008)
    assert(d5.aspect === 1008.0 / 768.0)
    assert(d5.col === "full")
  }

}

