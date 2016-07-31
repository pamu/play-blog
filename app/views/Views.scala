package views

import play.twirl.api.Html

object Views {
  def getLoaderHtml: Html = Html {
    s"""
       |<div class="preloader-wrapper big active">
       |    <div class="spinner-layer spinner-blue-only">
       |      <div class="circle-clipper left">
       |        <div class="circle"></div>
       |      </div><div class="gap-patch">
       |        <div class="circle"></div>
       |      </div><div class="circle-clipper right">
       |        <div class="circle"></div>
       |      </div>
       |    </div>
       |  </div>
       |
     """.stripMargin
  }
}
