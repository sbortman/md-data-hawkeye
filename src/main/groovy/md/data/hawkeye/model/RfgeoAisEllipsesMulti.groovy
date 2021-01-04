package md.data.hawkeye.model

import org.locationtech.jts.geom.MultiPolygon

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
class RfgeoAisEllipsesMulti {

  @Id
  @GeneratedValue
  Long id

  @Column(columnDefinition = 'geometry(MultiPolygon, 4326)')
  MultiPolygon geometry

  String passGroupId
  String downlinkedAt
  String createdAt
  String soi
  String mmsi
  String constellation
  String receivedAt
  Double semiMajor
  Double semiMinor
  Double orientation
  BigInteger numBursts
  Double ellipseArea

}
