<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <body>
                    <div date-role="header">
                        <h2>Parsed Log</h2>
                    </div>

                    <xsl:for-each select="log/line">
                                <a href="#attributes" data-toggle="collapse"><xsl:value-of select="type"/>></a>
                                <div id="attributes">
                                    <xsl:value-of select="start"/>
                                </div>
                    </xsl:for-each>

                    <div data-role="footer">
                        <h1> --  END OF FILE INFO HERE -- </h1>
                    </div>

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>