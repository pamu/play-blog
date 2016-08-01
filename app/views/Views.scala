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

  def gmailButton: Html = Html {
    s"""
      |<a id="gmail_signin" class="waves-effect waves-light btn deep-purple"><i class="material-icons left">mail</i>Signin with Gmail</a>
    """.stripMargin
  }

}
