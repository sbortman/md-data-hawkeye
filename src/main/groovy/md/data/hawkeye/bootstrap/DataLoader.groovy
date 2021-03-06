package md.data.hawkeye.bootstrap

import groovy.json.JsonSlurper
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import md.data.hawkeye.model.RfgeoAisEllipsesMulti
import md.data.hawkeye.repository.RfgeoAisEllipsesMultiRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.geom.PrecisionModel

import javax.inject.Singleton

@Singleton
class DataLoader {
  RfgeoAisEllipsesMultiRepository rfgeoAisEllipsesMultiRepository

  DataLoader( RfgeoAisEllipsesMultiRepository rfgeoAisEllipsesMultiRepository ) {
    this.rfgeoAisEllipsesMultiRepository = rfgeoAisEllipsesMultiRepository
  }

  @EventListener
  void onStartupEvent( StartupEvent event ) {

    if ( rfgeoAisEllipsesMultiRepository.count() == 0 ) {
      def file = "${ System.getenv( 'OSSIM_DATA' ) }/vector/1168_20200414_231758_RFGeo_AIS_ELLIPSES_MULTI/1168_20200414_231758_RFGeo_AIS_ELLIPSES_MULTI.geojson" as File
      def json = new JsonSlurper().parse( file )
      def count = 0

      GeometryFactory geometryFactory = new GeometryFactory( new PrecisionModel( PrecisionModel.FLOATING ), 4326 )

      json?.features?.each { f ->
        MultiPolygon geometry = f.geometry.coordinates.collect { mp ->
          geometryFactory.createMultiPolygon( mp.collect { p ->
            Coordinate[] x = p.collect { c -> c as Coordinate } as Coordinate[]

            geometryFactory.createPolygon( x )
          } as Polygon[] )
        }.first()

        def foo = new RfgeoAisEllipsesMulti(
            passGroupId: f?.properties?.pass_group_id,
            downlinkedAt: f?.properties?.downlinked_at,
            createdAt: f?.properties?.created_at,
            soi: f?.properties?.soi,
            mmsi: f?.properties?.mmsi,
            constellation: f?.properties?.constellation,
            receivedAt: f?.properties?.received_at,
            semiMajor: f?.properties?.semi_major?.toDouble(),
            semiMinor: f?.properties?.semi_minor?.toDouble(),
            orientation: f?.properties?.orientation?.toDouble(),
            numBursts: f?.properties?.num_bursts?.toBigInteger(),
            ellipseArea: f?.properties?.ellipse_area?.toDouble(),
            geometry: geometry
        )

        rfgeoAisEllipsesMultiRepository.save( foo )
        ++count
      }

      println count
    }
  }
}
