package md.data.hawkeye.repository

import md.data.hawkeye.model.RfgeoAisEllipsesMulti

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface RfgeoAisEllipsesMultiRepository extends CrudRepository<RfgeoAisEllipsesMulti, Long> {
}
